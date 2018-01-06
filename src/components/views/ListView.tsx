
import * as React from 'react';
import { withStyles, WithStyles, StyleRules } from 'material-ui/styles';
import Paper from 'material-ui/Paper';
import { LinearProgress } from 'material-ui/Progress';
import Table, { TableHead, TableBody, TableRow, TableCell } from 'material-ui/Table';

const styles: StyleRules = {
    root: {
        width: '100%',
        overflowX: 'auto',
    },
    table: {
        minWidth: 700,
    },
};

let id = 0;
function createData(name: any, calories: any, fat: any, carbs: any, protein: any) {
    id += 1;
    return { id, name, calories, fat, carbs, protein };
}

const data = [
  createData('Frozen yoghurt', 159, 6.0, 24, 4.0),
  createData('Ice cream sandwich', 237, 9.0, 37, 4.3),
  createData('Eclair', 262, 16.0, 24, 6.0),
  createData('Cupcake', 305, 3.7, 67, 4.3),
  createData('Gingerbread', 356, 16.0, 49, 3.9),
];

function BasicTableC(props: WithStyles) {
    const { classes } = props;

    return (
        <Paper className={classes.root}>
            <LinearProgress />
            <Table className={classes.table}>
            <TableHead>
                <TableRow>
                    <TableCell padding="dense">Dessert (100g serving)</TableCell>
                    <TableCell padding="dense" numeric>Calories</TableCell>
                    <TableCell padding="dense" numeric>Fat (g)</TableCell>
                    <TableCell padding="dense" numeric>Carbs (g)</TableCell>
                    <TableCell padding="dense" numeric>Protein (g)</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {data.map((n) => {
                    return (
                        <TableRow key={n.id} hover>
                            <TableCell padding="dense">{n.name}</TableCell>
                            <TableCell padding="dense" numeric>{n.calories}</TableCell>
                            <TableCell padding="dense" numeric>{n.fat}</TableCell>
                            <TableCell padding="dense" numeric>{n.carbs}</TableCell>
                            <TableCell padding="dense" numeric>{n.protein}</TableCell>
                        </TableRow>
                    );
                })}
            </TableBody>
            </Table>
        </Paper>
    );
}

export const ListView = withStyles(styles)(BasicTableC) as any;
