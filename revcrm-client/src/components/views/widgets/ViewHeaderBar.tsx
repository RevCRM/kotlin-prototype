
import * as React from "react"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles, Paper, IconButton, Icon, Typography } from "@material-ui/core"

export const styles = (theme: Theme) => createStyles({
    root: {
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
        marginRight: 12
    },
})

export interface IViewHeaderBarProps extends
    WithStyles<typeof styles> {
    backButtonEnabled: boolean
    title: string
    children?: any
}

function goBack() {
    history.go(-1)
}

export const ViewHeaderBar = withStyles(styles)((props: IViewHeaderBarProps) => (
    <Paper square className={props.classes.root}>
        {props.backButtonEnabled &&
            <div className={props.classes.backButtonContainer}>
                <IconButton color="inherit" onClick={goBack}>
                    <Icon>arrow_back</Icon>
                </IconButton>
            </div>}
        <Typography variant="h6" color="inherit" style={{ flexGrow: 1 }}>
            {props.title}
        </Typography>
        {props.children}
    </Paper>
))
