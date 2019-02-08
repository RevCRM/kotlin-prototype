
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles, Icon, Button } from "@material-ui/core"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../data/Metadata"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { DocumentNode } from "graphql"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queries"
import { IApolloClientProp, withApolloClient } from "../../graphql/withApolloClient"
import { LoadState } from "../utils/types"
import { IEntityMutationResult, getEntityMutation, getEntityMutationName, IEntityMutationOptions } from "../../graphql/mutations"
import { omitDeep } from "../../utils/objects"
import { RECORD_NAME_FIELD } from "../../graphql/helpers"
import { ViewHeaderBar } from "./widgets/ViewHeaderBar"
import { EntityViewMode, EntityContext, IEntityContext } from "../data/EntityContext"

export const styles = (theme: Theme) => createStyles({
    root: {
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

export interface IFormViewState {
    loadState: LoadState
    mode: EntityViewMode
    dirtyFields: string[]
}

export const FormView = withStyles(styles)(withMetadataContext(withViewManagerContext(withApolloClient(
    class extends React.Component<IFormViewProps, IFormViewState> {
        entityMeta: IEntityMetadata
        entityData: any
        query: DocumentNode

        constructor(props: any) {
            super(props)

            // TODO: This should not assume getEntity() returns an entity
            this.entityMeta = this.props.meta.getEntity(this.props.entity)!
            const fieldNames = this.entityMeta.fields.map((field) => field.name)

            this.query = getEntityQuery({
                meta: this.props.meta,
                entity: this.entityMeta,
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
            let mode: EntityViewMode = "view"
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

        cleanMutationData = (entityData: any) => {
            // clean data ready for mutations
            const newData = omitDeep(entityData, "__typename")
            this.deleteReadonlyFields(this.entityMeta, newData)
            return newData
        }

        deleteReadonlyFields(entity: IEntityMetadata, entityData: any) {
            entity.fields.forEach(field => {
                if (field.readonly) {
                    delete entityData[field.name]
                }
                else if (
                    entityData[field.name]
                    && (
                        field.type == "EmbeddedEntityField"
                        || field.type == "EmbeddedEntityListField"
                    )
                ) {
                    const relatedEntityName = field.constraints["Entity"]
                    const relatedEntity = this.props.meta.getEntity(relatedEntityName)!
                    if (field.type == "EmbeddedEntityField") {
                        this.deleteReadonlyFields(relatedEntity, entityData[field.name])
                    }
                    else if (field.type == "EmbeddedEntityListField" && entityData[field.name] instanceof Array) {
                        entityData[field.name].forEach((rowData: any) => {
                            this.deleteReadonlyFields(relatedEntity, rowData)
                        })
                    }
                }
                else if (
                    field.type == "ReferencedEntityField"
                ) {
                    if (entityData[field.name]) {
                        // make sure we dont try to save record_name field
                        delete entityData[field.name][RECORD_NAME_FIELD]
                    }
                }
            })
        }

        save = async (): Promise<IEntityMutationResult> => {
            const fieldNames = this.entityMeta.fields.map((field) => field.name)
            const idValue = this.entityData[this.entityMeta.idField]
            const isNew = !idValue
            console.log("isNew", isNew)
            if (isNew) {
                const data = this.cleanMutationData(this.entityData)
                const mutationOptions: IEntityMutationOptions = {
                    meta: this.props.meta,
                    entity: this.entityMeta,
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
                let data: any = {
                    [this.entityMeta.idField]: idValue
                }
                this.state.dirtyFields.forEach(field => {
                    data[field] = this.entityData[field]
                })
                data = this.cleanMutationData(data)
                const mutationOptions: IEntityMutationOptions = {
                    meta: this.props.meta,
                    entity: this.entityMeta,
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

            const entityContext: IEntityContext = {
                loadState,
                name: entity,
                meta: this.entityMeta,
                mode,
                data: this.entityData,
                dirtyFields,
                onFieldChange: this.onFieldChange,
                save: this.save
            }

            const perspectiveView = view.perspective.views[view.viewName]
            const viewTitle = perspectiveView.title

            return (
                <EntityContext.Provider value={entityContext}>
                    <ViewHeaderBar
                        backButtonEnabled={true}
                        title={viewTitle}
                    >
                        {mode == "view" && perspectiveView.actions && perspectiveView.actions.map((action, idx) => (
                            <Button key={idx} color="inherit" onClick={() => this.props.view.runAction(action)}>
                                <Icon className={classes.actionButtonIcon}>{action.icon}</Icon>
                                {action.label}
                            </Button>
                        ))}
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
                    </ViewHeaderBar>
                    <div style={{ margin: "16px auto", padding: "0 12px", maxWidth: "1024px" }}>
                        <Grid container spacing={16} className={classes.root}>
                            {this.props.children}
                        </Grid>
                    </div>
                </EntityContext.Provider>
            )
        }

    }))))
