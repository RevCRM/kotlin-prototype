import * as React from 'react';
import * as PropTypes from 'prop-types';

import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { withStyles, WithStyles } from '@material-ui/core/styles';
import { RouteComponentProps } from 'react-router-dom';
import { IModelProviderContext } from 'rev-ui/lib/provider/ModelProvider';
import { IModelManagerProp, withModelManager } from 'rev-ui';
import ArrowBack from '@material-ui/icons/ArrowBack';
import { IPerspective, IView, viewManager } from '../ViewManager';

export const styles = {
    root: {
        marginTop: 70,
        marginLeft: 'auto',
        marginRight: 'auto',
        maxWidth: 1000
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

        const { perspectiveName, perspectiveViewName } = newProps.match.params;
        const ctx = this.viewContext;

        console.log('new route', perspectiveName, perspectiveViewName);

        const perspective = viewManager.getPerspective(perspectiveName);
        const viewName = viewManager.getPerspectiveViewName(perspectiveName, perspectiveViewName);
        const view = viewManager.getView(viewName);

        ctx.perspective = perspective;
        ctx.view = view;

        const search = new URLSearchParams(newProps.location.search);
        const meta = newProps.modelManager.getModelMeta(ctx.view.model);

        if (meta.primaryKey) {
            ctx.primaryKeyValue = search.get(meta.primaryKey);
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
                    <Typography variant="title" color="inherit">
                        View not found
                    </Typography>
                </div>
            );
        }
        else {
            return (
                <div className={this.props.classes.root}>
                    <Toolbar>
                        <ArrowBack style={{marginRight: 10, cursor: 'pointer'}} onClick={() => this.props.history.goBack()} />
                        <Typography variant="title" color="inherit">
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

export const CRMViewManager = withModelManager(withStyles(styles)(CRMViewManagerC)) as any;
