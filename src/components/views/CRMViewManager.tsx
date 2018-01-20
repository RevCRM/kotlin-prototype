import * as React from 'react';
import * as PropTypes from 'prop-types';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { withStyles } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';
import { RouteComponentProps } from 'react-router-dom';
import { CRMListView } from './CRMListView';
import { CRMFormView } from './CRMFormView';
import { IModelProviderContext } from 'rev-forms-materialui/lib/provider/ModelProvider';
import { Field, IModelManagerProp, withModelManager } from 'rev-forms-materialui';

export interface IView {
    name: string;
    model: string;
    component: React.ReactNode;
}

export interface IPerspective {
    name: string;
    title: string;
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

const viewDef: IViewsDefinition = {
    perspectives: {
        accounts: {
            name: 'accounts',
            title: 'All Accounts',
            views: {
                list: 'account_list',
                form: 'account_form'
            }
        }
    },
    views: {
        account_list: {
            name: 'account_list',
            model: 'Account',
            component: (
                <CRMListView fields={[
                    'id',
                    'name',
                    'code',
                    'url']} />
            ),
        },
        account_form: {
            name: 'account_form',
            model: 'Account',
            component: (
                <CRMFormView>
                    <Field name="name" />
                    <Field name="code" colspan={3} />
                    <Field name="url" colspan={3} />
                </CRMFormView>
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

export interface ICRMViewManagerProps extends RouteComponentProps<any>, WithStyles<keyof typeof styles> {}

export interface ICRMViewContext {
    model: string;
    primaryKeyValue?: string;
}

export interface ICRMViewManagerContext {
    viewContext: ICRMViewContext;
}

class CRMViewManagerC extends React.Component<ICRMViewManagerProps & IModelManagerProp> {

    perspective: IPerspective;
    view: IView;

    viewContext: ICRMViewContext;

    constructor(props: ICRMViewManagerProps & IModelManagerProp, context: IModelProviderContext) {
        super(props, context);

        const { perspective, view } = this.getViewDefinition();

        this.perspective = perspective;
        this.view = view;
        this.viewContext = {
            model: view.model
        };

        const search = new URLSearchParams(this.props.location.search);
        const meta = this.props.modelManager.getModelMeta(this.view.model);
        if (meta.primaryKey) {
            this.viewContext.primaryKeyValue = search.get(meta.primaryKey);
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

    getChildContext(): ICRMViewManagerContext {
        return {
            viewContext: this.viewContext
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

export const CRMViewManager = withModelManager(withStyles(styles)(CRMViewManagerC));
