
import * as React from 'react'
import { Typography, Paper, Input, InputAdornment, Icon, Theme, createStyles, withStyles, WithStyles, Table, TableHead, TableRow, TableCell, TableBody, Toolbar, IconButton, Checkbox } from '@material-ui/core'
import { fade } from '@material-ui/core/styles/colorManipulator'

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    filterBox: {
        padding: 12,
        height: 60,
        display: 'flex',
        alignItems: 'center',
        color: '#fff',
        backgroundColor: theme.palette.primary.dark,
        zIndex: theme.zIndex.appBar - 1,
        position: 'relative'
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
    resultsBox: {
        backgroundColor: '#fff'
    },
    resultsToolbar: {
        justifyContent: 'space-between',
        borderBottom: '1px solid #EBEBEB'
    },
    pagination: {
        display: 'flex',
        alignItems: 'center'
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

export const AccountList = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div className={props.classes.root}>
        <Paper square className={props.classes.filterBox}>
            <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                Companies &amp; Contacts
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
        <div className={props.classes.resultsBox}>
            <Toolbar className={props.classes.resultsToolbar}>
                <Typography variant="title">Companies</Typography>
                <div className={props.classes.pagination}>
                    <Typography variant="caption">
                        1-20 of 42
                    </Typography>
                    <IconButton>
                        <Icon title="Previous Page">
                            keyboard_arrow_left
                        </Icon>
                    </IconButton>
                    <IconButton>
                        <Icon title="Next Page">
                            keyboard_arrow_right
                        </Icon>
                    </IconButton>
                </div>
            </Toolbar>
            <Table padding="dense">
                <TableHead>
                    <TableRow className={props.classes.resultsHeader}>
                        <TableCell padding="checkbox">
                            <Checkbox />
                        </TableCell>
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
                    <TableRow hover className={props.classes.resultsRow}>
                        <TableCell padding="checkbox">
                            <Checkbox />
                        </TableCell>
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
                    <TableRow hover className={props.classes.resultsRow}>
                        <TableCell padding="checkbox">
                            <Checkbox />
                        </TableCell>
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
                    <TableRow hover className={props.classes.resultsRow}>
                        <TableCell padding="checkbox">
                            <Checkbox />
                        </TableCell>
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
    </div>
))
