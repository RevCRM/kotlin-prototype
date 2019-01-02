
import * as React from "react"
import Grid from "@material-ui/core/Grid"
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles"
import { Theme, createStyles } from "@material-ui/core"

export const styles = (theme: Theme) => createStyles({
    root: {
        background: "#fff"
    }
})

export const FormView = withStyles(styles)((props: WithStyles<typeof styles> & {children: any}) => {
    return (
        <Grid container spacing={0} className={props.classes.root}>
            {props.children}
        </Grid>
    )
})
