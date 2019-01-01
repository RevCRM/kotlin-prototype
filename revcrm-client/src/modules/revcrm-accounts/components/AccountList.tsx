import * as React from "react"
import { Typography, Icon, Theme, createStyles, withStyles, WithStyles, Toolbar, IconButton } from "@material-ui/core"
import { fade } from "@material-ui/core/styles/colorManipulator"
import { ListView } from "../../../components/views/ListView"
import { FilterBar } from "../../../components/views/FilterBar"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    filterBox: {
        padding: 12,
        height: 60,
        display: "flex",
        alignItems: "center",
        color: "#fff",
        backgroundColor: theme.palette.primary.dark,
        zIndex: theme.zIndex.appBar - 1,
        position: "relative"
    },
    searchBox: {
        [theme.breakpoints.up("md")]: {
            minWidth: 350,
        },
    },
    searchInput: {
        color: "#fff",
        backgroundColor: fade(theme.palette.common.white, 0.15),
    },
    searchAdornment: {
        marginLeft: 4
    },
    resultsBox: {
        backgroundColor: "#fff"
    },
    resultsToolbar: {
        justifyContent: "space-between",
        borderBottom: "1px solid #EBEBEB"
    },
    pagination: {
        display: "flex",
        alignItems: "center"
    },
})

export const AccountList = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div className={props.classes.root}>
        <FilterBar
            title="Companies &amp; Contacts"
            entity="Account"
            onFilter={(where) => console.log("searching", where)}
        />
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
            <ListView
                entity="Account"
                fields={[
                    "org_name",
                    "first_name",
                    "last_name",
                    "email",
                    "phone"
                ]}
                where={{
                    _text: { _search: "test" }
                }}
            />
        </div>
    </div>
))
