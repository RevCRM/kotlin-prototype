
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles, Omit } from "@material-ui/core"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata } from "../meta/Metadata"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { DocumentNode } from "graphql"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queryhelpers"
import { IApolloClientProp, withApolloClient } from "../../graphql/withApolloClient"

export const styles = (theme: Theme) => createStyles({
    root: {
        background: "#fff"
    }
})

export type FormViewLoadState = "not_loaded" | "loading" | "loaded" | "load_error"

export interface IFormViewProps extends
                    IMetadataContextProp,
                    IViewManagerContextProp,
                    IApolloClientProp,
                    WithStyles<typeof styles> {
    entity: string
}

export interface IFormContext {
    loadState: FormViewLoadState
    entity: string
    entityData: any
}

export const FormContext = React.createContext<IFormContext>(null as any)

export const FormView = withStyles(styles)(withMetadataContext(withViewManagerContext(withApolloClient(
    class extends React.Component<IFormViewProps, IFormContext> {
    entityMeta: IEntityMetadata
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
            entity: this.props.entity,
            entityData: null
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
            const entityData = res.data[this.props.entity].results[0]
            console.log("Form data loaded", entityData)
            this.setState({
                loadState: "loaded",
                entityData
            })
        }
    }

    componentDidMount() {
        this.initialise()
    }

    render() {
        const { loadState } = this.state
        if (loadState != "loaded") return "Loading..."

        const formContext: IFormContext = {...this.state}

        return (
            <FormContext.Provider value={formContext}>
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
