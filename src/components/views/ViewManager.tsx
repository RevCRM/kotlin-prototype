import * as React from 'react';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { withStyles } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';
import { RouteComponentProps } from 'react-router-dom';
import { ListView } from './ListView';

const styles = {
    root: {
        marginTop: 70
    },
    viewSelector: {
        marginLeft: 30
    },
    viewWrapper: {
        margin: '10 20'
    }
};

export interface IViewsDefinition {
    [perspective: string]: {
        [viewName: string]: {
            title: string,
            component: any
        }
    };
}

const views: IViewsDefinition = {
    accounts: {
        list: {
            title: 'All Accounts',
            component: (contextProps: any) => (
                <ListView model="Account" fields={[
                    'id',
                    'name',
                    'code',
                    'url']} {...contextProps} />
            ),
        },
        form: {
            title: 'Edit Account',
            component: (contextProps: any) => (
                <p>Form view...</p>
            )
        }
    }
};

export interface IViewManagerProps extends WithStyles<any>, RouteComponentProps<any> {}

function ViewManagerC(props: IViewManagerProps) {
    const { perspective, view } = props.match.params;
    const search = new URLSearchParams(props.location.search);
    const contextProps = {
        viewContext: {
            id: search.get('id')
        }
    };

    if (!views[perspective] || !views[perspective][view]) {
        return (
            <div className={props.classes.root}>
                <Typography type="title" color="inherit">
                    View not found
                </Typography>
            </div>
        );
    }
    else {
        const viewComponent = views[perspective][view].component;
        return (
            <div className={props.classes.root}>
                <Toolbar>
                    <Typography type="title" color="inherit">
                        {views[perspective][view].title}
                    </Typography>
                </Toolbar>
                {viewComponent(contextProps)}
            </div>
        );
    }
}

export const ViewManager = withStyles(styles)(ViewManagerC);
