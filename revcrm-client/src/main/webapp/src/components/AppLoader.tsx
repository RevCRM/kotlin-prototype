
import * as React from "react"
import { Theme, createStyles, CircularProgress, withStyles, WithStyles, Typography, Button } from "@material-ui/core"
import { withAuthContext, IAuthContextProp } from "./auth/AuthContext"
import { CONFIG } from "../config"
import { IMetadataContextProp, withMetadataContext } from "./data/Metadata"

const styles = (theme: Theme) => createStyles({
    root: {
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-around",
        alignItems: "center",
        height: "100vh",
        color: "#fff",
        backgroundColor: theme.palette.primary.main,
    },
    loader: {
        color: "#fff"
    }
})

export interface IAppLoaderProps extends
        WithStyles<typeof styles>,
        IAuthContextProp,
        IMetadataContextProp {
    children: any
}

export const AppLoader = withAuthContext(withMetadataContext(withStyles(styles)((props: IAppLoaderProps) => {
    if (props.auth.authState == "not_logged_in") {
        return (
            <div className={props.classes.root}>
                <div style={{ textAlign: "center" }}>
                    <Typography variant="h5" color="inherit">
                        Welcome to {CONFIG.appTitle}
                    </Typography>
                    <Button variant="contained" size="large"
                        style={{ marginTop: 32, background: "#FFF" }}
                        onClick={props.auth.login}
                    >
                        Log in with Google
                    </Button>
                </div>
            </div>
        )
    }
    else if (props.auth.authState == "logged_in" && props.meta.metaState == "loaded") {
        return props.children
    }
    else {
        return (
            <div className={props.classes.root}>
                <CircularProgress className={props.classes.loader} />
            </div>
        )
    }
})))
