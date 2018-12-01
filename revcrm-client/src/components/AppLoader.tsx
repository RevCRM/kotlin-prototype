
import * as React from 'react';
import { Theme, createStyles, CircularProgress, withStyles, WithStyles, Typography } from '@material-ui/core';
import { withAuthContext, IAuthContextProp } from './auth/AuthContext';
import { CONFIG } from '../config';

const styles = (theme: Theme) => createStyles({
    root: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'space-around',
        alignItems: 'center',
        height: '100vh',
        color: '#fff',
        backgroundColor: theme.palette.primary.main,
    },
    loader: {
        color: '#fff'
    }
});

export interface IAppLoaderProps extends
        WithStyles<typeof styles>,
        IAuthContextProp {
    children: any;
}

export const AppLoader = withAuthContext(withStyles(styles)((props: IAppLoaderProps) => {
    if (props.auth.authState == 'not_logged_in') {
        return (
            <div className={props.classes.root}>
                <Typography variant="h3" color="inherit">
                    {CONFIG.appTitle}
                    <br />Ya need to log in bru
                </Typography>
            </div>
        );
    }
    else if (props.auth.authState == 'logged_in') {
        return props.children;
    }
    else {
        return (
            <div className={props.classes.root}>
                <CircularProgress className={props.classes.loader} />
            </div>
        );
    }
}));
