import * as React from 'react'
import { Theme, createStyles, WithStyles, withStyles, Table, TableHead, TableRow, TableCell, Checkbox, TableBody } from '@material-ui/core'

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    resultsHeader: {
        fontWeight: 'bold'
    },
    resultsRow: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.background.default,
        },
    },
})

export interface IListViewProps extends
                    WithStyles<typeof styles> {
    fields: string[]
    data: string[][]
}

export const ListView = withStyles(styles)(
    class extends React.Component<IListViewProps> {

    constructor(props: any) {
        super(props)
    }

    render() {
        return (
            <Table padding="dense" className={this.props.classes.root}>
                <TableHead>
                    <TableRow className={this.props.classes.resultsHeader}>
                        <TableCell padding="checkbox">
                            <Checkbox />
                        </TableCell>
                        {this.props.fields.map((field, idx) =>
                            <TableCell key={field}>
                                {field}
                            </TableCell>)}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {this.props.data.map((row, rowIdx) =>
                        <TableRow
                            key={rowIdx}
                            hover className={this.props.classes.resultsRow}
                        >
                            <TableCell padding="checkbox">
                                <Checkbox />
                            </TableCell>
                            {this.props.fields.map((field, fieldIdx) =>
                                <TableCell>
                                    {row[fieldIdx]}
                                </TableCell>
                            )}
                        </TableRow>
                    )}
                </TableBody>
            </Table>
        )
    }

})
