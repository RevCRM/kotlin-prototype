import * as React from "react"
import { Theme, createStyles, WithStyles, withStyles, Table, TableHead, TableRow, TableCell, Checkbox, TableBody, Toolbar, Typography, IconButton, Icon } from "@material-ui/core"
import { Query } from "react-apollo"
import { getEntityQuery, IEntityQueryResults } from "../../graphql/queries"
import { withMetadataContext, IMetadataContextProp, IEntityMetadata, IFieldMetadata } from "../data/Metadata"
import { DocumentNode } from "graphql"
import { withViewManagerContext, IViewManagerContextProp } from "./ViewManager"
import { Field } from "./fields/Field"
import { EntityContext, IEntityContext } from "../data/EntityContext"

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
    title?: string
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
        query: DocumentNode
        idField: IFieldMetadata
        listFieldMeta: IFieldMetadata[] = []
        listFieldComponents: React.ReactChild[]

        constructor(props: any) {
            super(props)

            this.state = {
                limit: DEFAULT_LIMIT,
                offset: 0
            }

            // TODO: This should neither be synchronous nor assume getEntity() returns an entity!
            this.entityMeta = this.props.meta.getEntity(this.props.entity)!

            this.listFieldComponents = React.Children.toArray(this.props.children)
                .filter(child => {
                    if (typeof child == "object" && child.type == Field) {
                        const fieldMeta = this.entityMeta.fields.find(
                            f => f.name == child.props.name)
                        if (fieldMeta) {
                            this.listFieldMeta.push(fieldMeta)
                            return true
                        }
                    }
                    return false
                })

            this.idField = this.entityMeta.fields.find(f => f.name == this.entityMeta.idField)!

            const fieldNames = this.listFieldMeta.map(f => f.name)
            fieldNames.unshift(this.idField.name)

            this.query = getEntityQuery({
                meta: this.props.meta,
                entity: this.entityMeta,
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
                const [perspective, view] = this.props.detailView.split("/")
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
                                    <Typography variant="title">{this.props.title}</Typography>
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
                                            {this.listFieldMeta.map((field) =>
                                                <TableCell key={field.name} className={this.props.classes.listHeaderCell}>
                                                    {field.label}
                                                </TableCell>)}
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {results.map((row: any, rowIdx: number) => {

                                            const entityContext: IEntityContext = {
                                                loadState: "loaded",
                                                name: this.props.entity,
                                                meta: this.entityMeta,
                                                mode: "view",
                                                data: row,
                                                dirtyFields: [],
                                                onFieldChange: () => null,
                                                save: () => null as any
                                            }

                                            return (
                                                <EntityContext.Provider key={rowIdx} value={entityContext}>
                                                    <TableRow
                                                        hover className={this.props.classes.listRow}
                                                        onClick={() => this.onRowClicked(row)}
                                                    >
                                                        <TableCell padding="checkbox">
                                                            <Checkbox/>
                                                        </TableCell>
                                                        {this.listFieldComponents.map((fieldComponent, cellIdx) => (
                                                            <TableCell key={cellIdx} className={this.props.classes.listCell}>
                                                                {fieldComponent}
                                                            </TableCell>
                                                        ))}
                                                    </TableRow>
                                                </EntityContext.Provider>
                                            )
                                        })}
                                    </TableBody>
                                </Table>
                            </div>
                        )
                    }}
                </Query>
            )
        }
    })))
