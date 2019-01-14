import * as React from "react"
import { Theme, createStyles, WithStyles, withStyles, Table, TableHead, TableRow, TableCell, Checkbox, TableBody, Toolbar, Typography, IconButton, Icon } from "@material-ui/core"
import { Query } from "react-apollo"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queryhelpers"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../meta/Metadata"
import { DocumentNode } from "graphql"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"

export const DEFAULT_LIMIT = 20

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    resultsToolbar: {
        justifyContent: "space-between",
        borderBottom: "1px solid #EBEBEB"
    },
    pagination: {
        display: "flex",
        alignItems: "center"
    },
    listHeader: {
        fontWeight: "bold"
    },
    listHeaderCell: {
        cursor: "default"
    },
    listRow: {
        "&:nth-of-type(odd)": {
            backgroundColor: theme.palette.background.default,
        },
    },
    listCell: {
        cursor: "default"
    },
})

export interface IListViewProps extends
                    IMetadataContextProp,
                    IViewManagerContextProp,
                    WithStyles<typeof styles> {
    entity: string
    fields: string[]
    where?: object
    detailView?: string
}

export interface IListViewState {
    limit: number
    offset: number
}

export const ListView = withStyles(styles)(withMetadataContext(withViewManagerContext(
    class extends React.Component<IListViewProps, IListViewState> {
    entityMeta: IEntityMetadata
    selectedFields: IFieldMetadata[]
    query: DocumentNode

    constructor(props: any) {
        super(props)

        this.state = {
            limit: DEFAULT_LIMIT,
            offset: 0
        }

        // TODO: This should neither be synchronous nor assume getEntity() returns an entity!
        this.entityMeta = this.props.meta.getEntity(this.props.entity)!
        this.selectedFields = this.props.fields.map(fieldName => {
            const match = this.entityMeta.fields.find(field => field.name == fieldName)
            if (!match) throw new Error(`Field '${fieldName}' does not exist on entity '${this.entityMeta.name}'`)
            return match
        })

        const fieldNames = [...this.props.fields]
        if (!fieldNames.includes("id")) {
            fieldNames.push("id")
        }

        this.query = getEntityQuery({
            entity: this.props.entity,
            fields: fieldNames,
        })
    }

    onForwardButtonPress = () => {
        this.setState({ offset: this.state.offset + this.state.limit })
    }

    onBackButtonPress = () => {
        this.setState({ offset: Math.max(this.state.offset - this.state.limit, 0) })
    }

    onRowClicked(row: any) {
        if (this.props.detailView) {
            const [ perspective, view ] = this.props.detailView.split("/")
            this.props.view.changePerspective(perspective, view, {
                id: row["id"]
            })
        }
    }

    render() {
        return (
            <Query<IEntityQueryResults>
                query={this.query}
                variables={{
                    where: this.props.where,
                    limit: this.state.limit,
                    offset: this.state.offset
                }}
            >
                {({ loading, error, data }) => {

                    if (loading) return null
                    if (error) return `Error! ${error.message}`
                    if (!data) return "No data returned"

                    const { results, meta } = data[this.props.entity]

                    const firstItemNumber = meta.totalCount ? meta.offset + 1 : 0
                    const lastItemNumber = Math.min(
                        meta.offset + meta.limit,
                        meta.totalCount
                    )
                    let forwardButtonDisabled = true
                    let backButtonDisabled = true
                    if (lastItemNumber < meta.totalCount) {
                        forwardButtonDisabled = false
                    }
                    if (firstItemNumber > 1) {
                        backButtonDisabled = false
                    }

                    return (
                        <div className={this.props.classes.root}>
                            <Toolbar className={this.props.classes.resultsToolbar}>
                                <Typography variant="title">Companies</Typography>
                                <div className={this.props.classes.pagination}>
                                    <Typography variant="caption">
                                        {firstItemNumber}-{lastItemNumber} of {meta.totalCount}
                                    </Typography>
                                    <IconButton
                                        onClick={this.onBackButtonPress}
                                        disabled={backButtonDisabled}
                                    >
                                        <Icon title="Previous Page">
                                            keyboard_arrow_left
                                        </Icon>
                                    </IconButton>
                                    <IconButton
                                        disabled={forwardButtonDisabled}
                                        onClick={this.onForwardButtonPress}
                                    >
                                        <Icon title="Next Page">
                                            keyboard_arrow_right
                                        </Icon>
                                    </IconButton>
                                </div>
                            </Toolbar>
                            <Table padding="dense" className={this.props.classes.root}>
                                <TableHead>
                                    <TableRow className={this.props.classes.listHeader}>
                                        <TableCell padding="checkbox">
                                            <Checkbox />
                                        </TableCell>
                                        {this.selectedFields.map((field) =>
                                            <TableCell key={field.name} className={this.props.classes.listHeaderCell}>
                                                {field.label}
                                            </TableCell>)}
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {results.map((row: any, rowIdx: number) =>
                                        <TableRow
                                            key={rowIdx}
                                            hover className={this.props.classes.listRow}
                                            onClick={() => this.onRowClicked(row)}
                                        >
                                            <TableCell padding="checkbox">
                                                <Checkbox />
                                            </TableCell>
                                            {this.selectedFields.map((field) =>
                                                <TableCell key={field.name} className={this.props.classes.listCell}>
                                                    {row[field.name]}
                                                </TableCell>
                                            )}
                                        </TableRow>
                                    )}
                                </TableBody>
                            </Table>
                        </div>
                    )
                }}
            </Query>
        )
    }
})))
