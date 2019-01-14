
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles, Omit, Paper, IconButton, Icon, Typography, Button } from "@material-ui/core"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../meta/Metadata"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { DocumentNode } from "graphql"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queryhelpers"
import { IApolloClientProp, withApolloClient } from "../../graphql/withApolloClient"
import { LoadState } from "../utils/types"
import { IEntityMutationResult } from "../../graphql/types"

export const styles = (theme: Theme) => createStyles({
    root: {
        background: "#fff"
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
})

export interface IFormViewProps extends
                    IMetadataContextProp,
                    IViewManagerContextProp,
                    IApolloClientProp,
                    WithStyles<typeof styles> {
    entity: string
}

export interface IFormViewState {
    loadState: LoadState
    dirtyFields: string[]
}

export interface IFormContext {
    loadState: LoadState
    entity: string
    entityData: any
    dirtyFields: string[]
    onFieldChange(field: IFieldMetadata, value: any): void
    save(): IEntityMutationResult
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
            dirtyFields: [],
        }
    }

    async initialise() {
        this.setState({ loadState: "loading" })
        const ctx = this.props.view.context
        const res = await this.props.client.query<IEntityQueryResults>({
            query: this.query,
            variables: {
                where: { id: ctx.id }
            }
        })
        if (res.errors && res.errors.length) {
            this.setState({ loadState: "load_error" })
            console.error("Failed to load data", res.errors)
        }
        else if (!res.data || !res.data[this.props.entity]
                || res.data[this.props.entity].results.length != 1) {
            this.setState({ loadState: "load_error" })
            console.error("Failed to load data", res.errors)
        }
        else {
            this.entityData = res.data[this.props.entity].results[0]
            console.log("Form data loaded", this.entityData)
            this.setState({
                loadState: "loaded",
                dirtyFields: []
            })
        }
    }

    componentDidMount() {
        this.initialise()
    }

    onFieldChange = (field: IFieldMetadata, value: any) => {
        console.log("field changed", field, value)
        Object.assign(this.entityData, { [field.name]: value })
        if (!this.state.dirtyFields.includes(field.name)) {
            const dirtyFields = [...this.state.dirtyFields, field.name]
            this.setState({ dirtyFields })
        }
    }

    save = (): IEntityMutationResult => {
        const idValue = this.entityData[this.entityMeta.idField]
        const isNew = Boolean(idValue)
        console.log("isNew", isNew)
        const updateData: any = {
            [this.entityMeta.idField]: idValue
        }
        this.state.dirtyFields.forEach(field =>
            updateData[field] = this.entityData[field]
        )
        console.log("update data", updateData)
        return null as any
    }

    goBack() {
        history.go(-1)
    }

    render() {
        const { loadState, dirtyFields } = this.state
        const { entity } = this.props
        if (loadState != "loaded") return null

        const formContext: IFormContext = {
            loadState,
            entity,
            entityData: this.entityData,
            dirtyFields,
            onFieldChange: this.onFieldChange,
            save: this.save
        }

        return (
            <FormContext.Provider value={formContext}>
                <Paper square className={this.props.classes.formHeader}>
                    <div className={this.props.classes.backButtonContainer}>
                        <IconButton color="inherit" onClick={this.goBack}>
                            <Icon>arrow_back</Icon>
                        </IconButton>
                    </div>
                    <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                        Edit {this.entityMeta.name}
                    </Typography>
                    <Button color="inherit" onClick={this.save}>
                        Save
                    </Button>
                </Paper>
                <Grid container spacing={0} className={this.props.classes.root}>
                    {this.props.children}
                </Grid>
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
