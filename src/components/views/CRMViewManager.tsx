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
import { ViewManager, FormView, Field } from 'rev-forms-materialui';

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
                <CRMListView model="Account" fields={[
                    'id',
                    'name',
                    'code',
                    'url']} />
            ),
        },
        account_form: {
            name: 'account_form',
            component: (
                <CRMFormView model="Account">
                    <FormView model="Account">
                        <Field name="name" />
                        <Field name="code" colspan={3} />
                        <Field name="url" colspan={3} />
                    </FormView>
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

class CRMViewManagerC extends React.Component<ICRMViewManagerProps> {

    perspective: IPerspective;
    view: IView;
    primaryKeyValue: string;

    context: IModelProviderContext;
    static contextTypes = {
        modelManager: PropTypes.object
    };

    constructor(props: ICRMViewManagerProps, context: IModelProviderContext) {
        super(props, context);

        const { perspective, view } = this.getViewDefinition();
        this.perspective = perspective;
        this.view = view;

        const search = new URLSearchParams(this.props.location.search);
        const manager = this.context.modelManager;

        const modelMeta = manager.getModelMeta(this.perspective.model);
        if (modelMeta.primaryKey) {
            this.primaryKeyValue = search.get(modelMeta.primaryKey);
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
                    <ViewManager model={this.perspective.model} primaryKeyValue={this.primaryKeyValue}>
                        <Toolbar>
                            <Typography type="title" color="inherit">
                                {this.perspective.title}
                            </Typography>
                        </Toolbar>
                        <div className={this.props.classes.viewWrapper}>
                            {this.view.component}
                        </div>
                    </ViewManager>
                </div>
            );
        }
    }
}

export const CRMViewManager = withStyles(styles)(CRMViewManagerC);
