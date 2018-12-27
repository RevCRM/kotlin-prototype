import * as React from "react"
import gql from "graphql-tag"
import { Theme, createStyles, WithStyles, withStyles, Table, TableHead, TableRow, TableCell, Checkbox, TableBody } from "@material-ui/core"
import { Query } from "react-apollo"

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
                    WithStyles<typeof styles> {
    fields: string[]
    data: string[][]
}

const ACCOUNT_QUERY = gql`
    query {
        Account {
            results {
                id
                org_name
                first_name
                last_name
            }
        }
    }
`

export const ListView = withStyles(styles)((props: IListViewProps) =>

    <Query query={ACCOUNT_QUERY}>
        {({ loading, error, data }) => {

            const fields = [
                "id",
                "org_name",
                "first_name",
                "last_name",
            ]

            if (loading) return "Loading..."
            if (error) return `Error! ${error.message}`
            return (
                <Table padding="dense" className={props.classes.root}>
                    <TableHead>
                        <TableRow className={props.classes.resultsHeader}>
                            <TableCell padding="checkbox">
                                <Checkbox />
                            </TableCell>
                            {fields.map((field, idx) =>
                                <TableCell key={field}>
                                    {field}
                                </TableCell>)}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data.Account.results.map((row: any, rowIdx: number) =>
                            <TableRow
                                key={rowIdx}
                                hover className={props.classes.resultsRow}
                            >
                                <TableCell padding="checkbox">
                                    <Checkbox />
                                </TableCell>
                                {fields.map((field, fieldIdx) =>
                                    <TableCell key={fieldIdx}>
                                        {row[field]}
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
