import * as React from 'react';
import * as PropTypes from 'prop-types';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { withStyles } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';
import { RouteComponentProps } from 'react-router-dom';
import { ListView } from './ListView';
import { FormView } from './FormView';
import { IModelProviderContext } from 'rev-forms-materialui/lib/provider/ModelProvider';
import { IModelMeta } from 'rev-models';

export interface IView {
    name: string;
    component: React.ReactNode;
}

export interface IPerspective {
    name: string;
    title: string;
    model: string;
    views: {
        [viewName: string]: string;
    };
}

export interface IViewsDefinition {
    perspectives: {
        [perspectiveName: string]: IPerspective;
    };
    views: {
        [viewName: string]: IView;
    };
}

export interface IViewContext {
    modelMeta: IModelMeta<any>;
    primaryKeyValue: string;
}

const viewDef: IViewsDefinition = {
    perspectives: {
        accounts: {
            name: 'accounts',
            title: 'All Accounts',
            model: 'Account',
            views: {
                list: 'account_list',
                form: 'account_form'
            }
        }
    },
    views: {
        account_list: {
            name: 'account_list',
            component: (
                <ListView model="Account" fields={[
                    'id',
                    'name',
                    'code',
                    'url']} />
            ),
        },
        account_form: {
            name: 'account_form',
            component: (
                <FormView model="Account" />
            )
        }
    }
};

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

export interface IViewManagerProps extends RouteComponentProps<any>, WithStyles<keyof typeof styles> {}

class ViewManagerC extends React.Component<IViewManagerProps> {

    perspective: IPerspective;
    view: IView;

    modelMeta: IModelMeta<any>;
    primaryKeyValue: string;

    context: IModelProviderContext;
    static contextTypes = {
        modelManager: PropTypes.object
    };

    constructor(props: IViewManagerProps, context: IModelProviderContext) {
        super(props, context);
        const { perspective, view } = this.getViewDefinition();
        this.perspective = perspective;
        this.view = view;

        const search = new URLSearchParams(this.props.location.search);
        const manager = this.context.modelManager;

        this.modelMeta = manager.getModelMeta(this.perspective.model);
        if (this.modelMeta.primaryKey) {
            this.primaryKeyValue = search.get(this.modelMeta.primaryKey);
        }
    }

    getViewDefinition(): { perspective: IPerspective, view: IView } {
        const { perspective, view } = this.props.match.params;

        if (!viewDef.perspectives[perspective]
            || !viewDef.perspectives[perspective].views[view]
            || !viewDef.views[viewDef.perspectives[perspective].views[view]]) {
            return {
                perspective: null,
                view: null,
            };
        }
        else {
            return {
                perspective: viewDef.perspectives[perspective],
                view: viewDef.views[viewDef.perspectives[perspective].views[view]]
            };
        }
    }

    static childContextTypes = {
        viewContext: PropTypes.object
    };

    getChildContext(): { viewContext: IViewContext } {
        return {
            viewContext: {
                modelMeta: this.modelMeta,
                primaryKeyValue: this.primaryKeyValue
            }
        };
    }

    render() {
        if (!this.view.component) {
            return (
                <div className={this.props.classes.root}>
                    <Typography type="title" color="inherit">
                        View not found
                    </Typography>
                </div>
            );
        }
        else {
            return (
                <div className={this.props.classes.root}>
                    <Toolbar>
                        <Typography type="title" color="inherit">
                            {this.perspective.title}
                        </Typography>
                    </Toolbar>
                    <div className={this.props.classes.viewWrapper}>
                        {this.view.component}
                    </div>
                </div>
            );
        }
    }
}

export const ViewManager = withStyles(styles)(ViewManagerC);
