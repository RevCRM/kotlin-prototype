
import * as React from 'react';
import { Typography, Paper, Input, InputAdornment, Icon, Theme, createStyles, withStyles, WithStyles, Table, TableHead, TableRow, TableCell, TableBody } from '@material-ui/core';
import { fade } from '@material-ui/core/styles/colorManipulator';

export const styles = (theme: Theme) => createStyles({
    root: {
        padding: 12,
        display: 'flex',
        color: '#fff',
        backgroundColor: theme.palette.primary.dark
    },
    searchBox: {
        [theme.breakpoints.up('md')]: {
            minWidth: 350,
        },
    },
    searchInput: {
        color: '#fff',
        backgroundColor: fade(theme.palette.common.white, 0.15),
    },
    searchAdornment: {
        marginLeft: 4
    },
});

export const AccountList = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div>
        <Paper square className={props.classes.root}>
            <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                Accounts
            </Typography>
            <Input
                className={props.classes.searchBox}
                classes={{
                    root: props.classes.searchInput,
                }}
                disableUnderline={true}
                placeholder="Search"
                startAdornment={
                    <InputAdornment position="start" className={props.classes.searchAdornment}>
                        <Icon>search</Icon>
                    </InputAdornment>
                }
            />
        </Paper>
        <Table padding="none">
            <TableHead>
                <TableRow>
                    <TableCell>
                        Task
                    </TableCell>
                    <TableCell>
                        Related To
                    </TableCell>
                    <TableCell>
                        Related To
                    </TableCell>
                    <TableCell>
                        Related To
                    </TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                <TableRow>
                    <TableCell>
                        Prepare Proposal
                    </TableCell>
                    <TableCell>
                        Opportunity 105: New Build
                    </TableCell>
                    <TableCell>
                        Opportunity 105: New Build
                    </TableCell>
                    <TableCell>
                        Opportunity 105: New Build
                    </TableCell>
                </TableRow>
                <TableRow>
                    <TableCell>
                        Prepare Proposal
                    </TableCell>
                    <TableCell>
                        Opportunity 107: Cable Street Works
                    </TableCell>
                    <TableCell>
                        Opportunity 107: Cable Street Works
                    </TableCell>
                    <TableCell>
                        Opportunity 107: Cable Street Works
                    </TableCell>
                </TableRow>
                <TableRow>
                    <TableCell>
                        Buy Milk
                    </TableCell>
                    <TableCell>
                        Personal
                    </TableCell>
                    <TableCell>
                        Personal
                    </TableCell>
                    <TableCell>
                        Personal
                    </TableCell>
                </TableRow>
            </TableBody>
        </Table>
    </div>
));
