
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles } from "@material-ui/core"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata } from "../meta/Metadata"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { DocumentNode } from "graphql"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queryhelpers"
import { Query } from "react-apollo"

export const styles = (theme: Theme) => createStyles({
    root: {
        background: "#fff"
    }
})

export interface IFormViewProps extends
                    IMetadataContextProp,
                    IViewManagerContextProp,
                    WithStyles<typeof styles> {
    entity: string
}

export const FormView = withStyles(styles)(withMetadataContext(withViewManagerContext(
    class extends React.Component<IFormViewProps> {
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
    }

    render() {

        const id = this.props.viewManagerCtx.viewContext.id

        return (
            <Query<IEntityQueryResults>
                query={this.query}
                variables={{
                    where: { id }
                }}
            >
                {({ loading, error, data }) => {

                    if (loading) return "Loading..."
                    if (error) return `Error! ${error.message}`
                    if (!data) return "No data returned"

                    return (
                        <Grid container spacing={0} className={this.props.classes.root}>
                            {this.props.children}
                        </Grid>
                    )
                }}
            </Query>
        )
    }
})))
