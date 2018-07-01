
import * as React from 'react';
import { createStyles, withStyles, WithStyles, Theme } from '@material-ui/core/styles';
import { Route } from 'react-router-dom';
import { UserLoginForm } from '../../models/User/views/UserLoginForm';
import { CRMViewManager } from '../../views/CRMViewManager';

const styles = (theme: Theme) => createStyles({
    root: {
        marginTop: 75,
        '@media print': {
            marginTop: 0
        }
    },
});

export const Routes = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <div className={props.classes.root}>
        <Route path="/login" component={UserLoginForm} />
        <Route path="/:perspectiveName/:perspectiveViewName" component={CRMViewManager} />
    </div>
));
