
import * as React from "react"
import { Typography, Paper, Icon, Theme, createStyles, withStyles, WithStyles, IconButton, Button } from "@material-ui/core"

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
    backButtonContainer: {
        marginTop: -12,
        marginBottom: -12,
    },
    form: {}
})

export const AccountForm = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div className={props.classes.root}>
        <Paper square className={props.classes.filterBox}>
            <div className={props.classes.backButtonContainer}>
                <IconButton color="inherit">
                    <Icon>keyboard_arrow_left</Icon>
                </IconButton>
            </div>
            <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
                New Account
            </Typography>
            <Button color="inherit">
                Save
            </Button>
        </Paper>
        <div className={props.classes.form}>
            <h2>Form Goes Here...</h2>
        </div>
    </div>
))
