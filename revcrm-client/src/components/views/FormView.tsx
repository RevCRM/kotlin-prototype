
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles, Omit, Paper, IconButton, Icon, Typography, Button } from "@material-ui/core"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../meta/Metadata"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { DocumentNode } from "graphql"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queries"
import { IApolloClientProp, withApolloClient } from "../../graphql/withApolloClient"
import { LoadState } from "../utils/types"
import { IEntityMutationResult, getEntityMutation, getEntityMutationName, IEntityMutationOptions } from "../../graphql/mutations"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    formHeader: {
        padding: 12,
        height: 60,
        display: "flex",
        alignItems: "center",
        color: "#fff",
        backgroundColor: theme.palette.primary.dark,
        zIndex: theme.zIndex.appBar - 1,
        position: "relative"
    },
    backButtonContainer: {
        marginTop: -12,
        marginBottom: -12,
        marginRight: 12
    },
    actionButton: {
        marginLeft: 12
    },
    actionButtonIcon: {
        marginRight: theme.spacing.unit
    }
})

export interface IFormViewProps extends
    IMetadataContextProp,
    IViewManagerContextProp,
    IApolloClientProp,
    WithStyles<typeof styles> {
    entity: string
}

export type FormMode = "view" | "edit"

export interface IFormViewState {
    loadState: LoadState
    mode: FormMode
    dirtyFields: string[]
}

export interface IFormContext {
    loadState: LoadState
    mode: FormMode
    entity: string
    entityData: any
    dirtyFields: string[]
    onFieldChange(field: IFieldMetadata, value: any): void
    save(): Promise<IEntityMutationResult>
}

export const FormContext = React.createContext<IFormContext>(null as any)

export const FormView = withStyles(styles)(withMetadataContext(withViewManagerContext(withApolloClient(
    class extends React.Component<IFormViewProps, IFormViewState> {
        entityMeta: IEntityMetadata
        entityData: any
        query: DocumentNode

        constructor(props: any) {
            super(props)

            // TODO: This should neither be synchronous nor assume getEntity() returns an entity!
            this.entityMeta = this.props.meta.getEntity(this.props.entity)!
            const fieldNames = this.entityMeta.fields.map((field) => field.name)

            this.query = getEntityQuery({
                entity: this.props.entity,
                fields: fieldNames,
            })

            this.state = {
                loadState: "not_loaded",
                mode: "view",
                dirtyFields: [],
            }
        }

        async initialise() {
            this.setState({ loadState: "loading" })
            const ctx = this.props.view.context
            let mode: FormMode = "view"
            try {
                if (ctx.id) {
                    const res = await this.props.client.query<IEntityQueryResults>({
                        query: this.query,
                        variables: {
                            where: { id: ctx.id }
                        }
                    })
                    if (res.errors && res.errors.length) {
                        throw new Error("Failed to load data: " + JSON.stringify(res.errors))
                    }
                    else if (!res.data || !res.data[this.props.entity]
                        || res.data[this.props.entity].results.length != 1) {
                        throw new Error("API did not return expected data: " + JSON.stringify(res))
                    }
                    else {
                        this.entityData = res.data[this.props.entity].results[0]
                    }
                }
                else {
                    // new record
                    this.entityData = {}
                    mode = "edit"
                }
            }
            catch (e) {
                console.log(e)
                this.setState({ loadState: "load_error" })
            }
            this.setState({
                loadState: "loaded",
                mode,
                dirtyFields: []
            })
        }

        componentDidMount() {
            this.initialise()
        }

        onFieldChange = (field: IFieldMetadata, value: any) => {
            console.log("field changed", field.name, value)
            Object.assign(this.entityData, { [field.name]: value })
            if (!this.state.dirtyFields.includes(field.name)) {
                const dirtyFields = [...this.state.dirtyFields, field.name]
                this.setState({ dirtyFields })
            }
        }

        onEditPressed = () => {
            this.setState({ mode: "edit" })
        }

        onCancelPressed = () => {
            this.setState({ mode: "view" })
        }

        save = async (): Promise<IEntityMutationResult> => {
            const fieldNames = this.entityMeta.fields.map((field) => field.name)
            const idValue = this.entityData[this.entityMeta.idField]
            const isNew = !idValue
            console.log("isNew", isNew)
            if (isNew) {
                const data = { ...this.entityData }
                const mutationOptions: IEntityMutationOptions = {
                    entity: this.props.entity,
                    operation: "create",
                    resultFields: fieldNames
                }
                const mutation = getEntityMutation(mutationOptions)
                const mutationName = getEntityMutationName(mutationOptions)
                const res = await this.props.client.mutate({
                    mutation,
                    variables: { data }
                })
                const resData: IEntityMutationResult = res.data as any  // mutate() typings dont seem to set this
                if (res.errors && res.errors.length) {
                    throw new Error("Mutation failed: " + JSON.stringify(res.errors))
                }
                else if (!resData || !resData[mutationName]
                    || !resData[mutationName].validation) {
                    throw new Error("API did not return expected data: " + JSON.stringify(res))
                }
                else if (resData[mutationName].validation.hasErrors) {
                    alert("validation errors: " + JSON.stringify(resData[mutationName].validation))
                    throw new Error("Validation errors occured: " + JSON.stringify(resData[mutationName].validation))
                }
                else {
                    // load returned data
                    this.entityData = resData[mutationName].result
                }
            }
            else {
                const data: any = {
                    [this.entityMeta.idField]: idValue
                }
                this.state.dirtyFields.forEach(field =>
                    data[field] = this.entityData[field]
                )
                const mutationOptions: IEntityMutationOptions = {
                    entity: this.props.entity,
                    operation: "update",
                    resultFields: fieldNames
                }
                const mutation = getEntityMutation(mutationOptions)
                const mutationName = getEntityMutationName(mutationOptions)
                const res = await this.props.client.mutate({
                    mutation,
                    variables: { data }
                })
                const resData: IEntityMutationResult = res.data as any  // mutate() typings dont seem to set this
                if (res.errors && res.errors.length) {
                    throw new Error("Mutation failed: " + JSON.stringify(res.errors))
                }
                else if (!resData || !resData[mutationName]
                    || !resData[mutationName].validation) {
                    throw new Error("API did not return expected data: " + JSON.stringify(res))
                }
                else if (resData[mutationName].validation.hasErrors) {
                    alert("validation errors: " + JSON.stringify(resData[mutationName].validation))
                    throw new Error("Validation errors occured: " + JSON.stringify(resData[mutationName].validation))
                }
                else {
                    // load returned data
                    this.entityData = resData[mutationName].result
                }
            }
            this.setState({ mode: "view" })
            return null as any
        }

        goBack() {
            history.go(-1)
        }

        render() {
            const { loadState, mode, dirtyFields } = this.state
            const { entity, view, classes } = this.props
            if (loadState != "loaded") return null

            const formContext: IFormContext = {
                loadState,
                entity,
                mode,
                entityData: this.entityData,
                dirtyFields,
                onFieldChange: this.onFieldChange,
                save: this.save
            }

            return (
                <FormContext.Provider value={formContext}>
                    <Paper square className={classes.formHeader}>
                        <div className={classes.backButtonContainer}>
                            <IconButton color="inherit" onClick={this.goBack}>
                                <Icon>arrow_back</Icon>
                            </IconButton>
                        </div>
                        <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                            {(view.context.id ? "" : "New ") + this.entityMeta.name}
                        </Typography>
                        {mode == "view" &&
                            <Button color="inherit" onClick={this.onEditPressed}>
                                <Icon className={classes.actionButtonIcon}>edit</Icon>
                                Edit
                            </Button>}
                        {mode == "edit" && <>
                            <Button variant="contained" color="default" className={classes.actionButton}
                                onClick={this.onCancelPressed}>
                                Cancel
                            </Button>
                            <Button variant="contained" color="secondary" className={classes.actionButton}
                                onClick={this.save}>
                                <Icon className={classes.actionButtonIcon}>done</Icon>
                                Save
                            </Button>
                        </>}

                    </Paper>
                    <div style={{ margin: 16 }}>
                        <Grid container spacing={16} className={classes.root}>
                            {this.props.children}
                        </Grid>
                    </div>
                    </FormContext.Provider>
            )
        }

    }))))

export interface IFormContextProp {
    form: IFormContext
}

export function withFormContext<
    TComponentProps extends IFormContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IFormContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: any): React.ReactElement<TComponentProps> => (
        <FormContext.Consumer>{(form) => (
            <Component form={form} {...props} />
        )}</FormContext.Consumer>
    )
}
