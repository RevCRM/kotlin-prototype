import * as React from "react"
import { Theme, createStyles, WithStyles, withStyles, Table, TableHead, TableRow, TableCell, Checkbox, TableBody } from "@material-ui/core"
import { Query } from "react-apollo"
import { getEntityQuery } from "../../graphql/queryhelpers"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../meta/Metadata"
import { DocumentNode } from "graphql"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    resultsHeader: {
        fontWeight: "bold"
    },
    resultsRow: {
        "&:nth-of-type(odd)": {
            backgroundColor: theme.palette.background.default,
        },
    },
})

export interface IListViewProps extends
                    IMetadataContextProp,
                    WithStyles<typeof styles> {
    entity: string
    fields: string[]
    where?: object
}

export const ListView = withStyles(styles)(withMetadataContext(
    class extends React.Component<IListViewProps> {
    entityMeta: IEntityMetadata
    selectedFields: IFieldMetadata[]
    query: DocumentNode

    constructor(props: any) {
        super(props)
        // TODO: This should neither be synchronous nor assume getEntity() returns an entity!
        this.entityMeta = this.props.meta.getEntity(this.props.entity)!
        this.selectedFields = this.props.fields.map(fieldName => {
            const match = this.entityMeta.fields.find(field => field.name == fieldName)
            if (!match) throw new Error(`Field '${fieldName}' does not exist on entity '${this.entityMeta.name}'`)
            return match
        })

        this.query = getEntityQuery({
            entity: this.props.entity,
            fields: this.props.fields
        })
    }

    render() {
        return (
            <Query query={this.query} variables={{ where: this.props.where }}>
                {({ loading, error, data }) => {

                    if (loading) return "Loading..."
                    if (error) return `Error! ${error.message}`
                    return (
                        <Table padding="dense" className={this.props.classes.root}>
                            <TableHead>
                                <TableRow className={this.props.classes.resultsHeader}>
                                    <TableCell padding="checkbox">
                                        <Checkbox />
                                    </TableCell>
                                    {this.selectedFields.map((field) =>
                                        <TableCell key={field.name}>
                                            {field.label}
                                        </TableCell>)}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {data.Account.results.map((row: any, rowIdx: number) =>
                                    <TableRow
                                        key={rowIdx}
                                        hover className={this.props.classes.resultsRow}
                                    >
                                        <TableCell padding="checkbox">
                                            <Checkbox />
                                        </TableCell>
                                        {this.selectedFields.map((field) =>
                                            <TableCell key={field.name}>
                                                {row[field.name]}
                                            </TableCell>
                                        )}
                                    </TableRow>
                                )}
                            </TableBody>
                        </Table>
                    )
                }}
            </Query>
        )
    }
}))
