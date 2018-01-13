import * as React from 'react';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { withStyles, StyleRules } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';
import { RouteComponentProps } from 'react-router-dom';
import { ListView } from './ListView';

const styles: StyleRules = {
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
    [perspectiveName: string]: {
        title: string;
        model: string;
        views: {
            [viewName: string]: {
                component: React.ReactNode
            }
        }
    };
}

const views: IViewsDefinition = {
    accounts: {
        title: 'All Accounts',
        model: 'Account',
        views: {
            list: {
                component: (
                    <ListView model="Account" fields={[
                        'id',
                        'name',
                        'code',
                        'url']} />
                ),
            },
            form: {
                component: (
                    <p>Form view...</p>
                )
            }
        },
    }
};

export interface IViewManagerProps extends WithStyles<any>, RouteComponentProps<any> {}

class ViewManagerC extends React.Component<IViewManagerProps> {

    render() {
        const { perspective, view } = this.props.match.params;
        // const search = new URLSearchParams(props.location.search);
        // // const contextProps = {
        // //     viewContext: {
        // //         id: search.get('id')
        // //     }
        // // };

        if (!views[perspective] || !views[perspective].views[view]) {
            return (
                <div className={this.props.classes.root}>
                    <Typography type="title" color="inherit">
                        View not found
                    </Typography>
                </div>
            );
        }
        else {
            const viewComponent = views[perspective].views[view].component;
            return (
                <div className={this.props.classes.root}>
                    <Toolbar>
                        <Typography type="title" color="inherit">
                            {views[perspective].title}
                        </Typography>
                    </Toolbar>
                    {viewComponent}
                </div>
            );
        }
    }
}

export const ViewManager = withStyles(styles)(ViewManagerC);
