import * as React from 'react';
import * as PropTypes from 'prop-types';

import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import { withStyles } from 'material-ui/styles';
import { WithStyles } from 'material-ui/styles/withStyles';
import { RouteComponentProps } from 'react-router-dom';
import { CRMListView } from './CRMListView';
import { CRMFormView } from './CRMFormView';
import { IModelProviderContext } from 'rev-ui/lib/provider/ModelProvider';
import { Field, IModelManagerProp, withModelManager } from 'rev-ui';

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
            title: 'Accounts',
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
                    'url']} detailView="accounts/form" />
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
        margin: '0 20'
    }
};

export interface ICRMViewManagerProps extends RouteComponentProps<any>, WithStyles<keyof typeof styles> {}

export interface ICRMViewContextArgs {
    [key: string]: string;
}

export interface ICRMViewContext {
    perspective: IPerspective;
    view: IView;
    primaryKeyValue?: string;

    changePerspective(perspectiveName: string, viewName: string, args?: ICRMViewContextArgs): void;
}

export interface ICRMViewManagerContext {
    viewContext: ICRMViewContext;
}

class CRMViewManagerC extends React.Component<ICRMViewManagerProps & IModelManagerProp> {

    viewContext: ICRMViewContext;

    constructor(props: ICRMViewManagerProps & IModelManagerProp, context: IModelProviderContext) {
        super(props, context);

        this.viewContext = {
            perspective: null,
            view: null,
            primaryKeyValue: null,
            changePerspective: (p, v, k) => this.changePerspective(p, v, k)
        };

        this.updateViewContextFromProps(props);
    }

    componentWillReceiveProps(newProps: ICRMViewManagerProps & IModelManagerProp) {
        this.updateViewContextFromProps(newProps);
    }

    updateViewContextFromProps(newProps: ICRMViewManagerProps & IModelManagerProp) {

        const { perspectiveName, viewName } = newProps.match.params;
        const ctx = this.viewContext;

        console.log('new route', perspectiveName, viewName);
        if (viewDef.perspectives[perspectiveName]
            && viewDef.perspectives[perspectiveName].views[viewName]
            && viewDef.views[viewDef.perspectives[perspectiveName].views[viewName]]) {

            ctx.perspective = viewDef.perspectives[perspectiveName];
            ctx.view = viewDef.views[viewDef.perspectives[perspectiveName].views[viewName]];
        }

        if (ctx.perspective && ctx.view) {
            console.log('found new view');
            const search = new URLSearchParams(newProps.location.search);
            const meta = newProps.modelManager.getModelMeta(ctx.view.model);

            if (meta.primaryKey) {
                ctx.primaryKeyValue = search.get(meta.primaryKey);
            }
        }
    }

    changePerspective(perspectiveName: string, viewName: string, args?: ICRMViewContextArgs) {
        const baseUrl = `/${perspectiveName}/${viewName}`;
        const params = new URLSearchParams();
        if (args) {
            Object.keys(args).forEach((key) => {
                params.append(key, args[key]);
            });
        }
        console.log('going to', `${baseUrl}?${params.toString()}`);
        this.props.history.push(`${baseUrl}?${params}`);
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
        const { perspective, view } = this.viewContext;

        if (!perspective || !view) {
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
                            {perspective.title}
                        </Typography>
                    </Toolbar>
                    <div className={this.props.classes.viewWrapper}>
                        {view.component}
                    </div>
                </div>
            );
        }
    }
}

export const CRMViewManager = withModelManager(withStyles(styles)(CRMViewManagerC));
