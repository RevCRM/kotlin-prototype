import * as React from 'react';

import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { withStyles, WithStyles } from '@material-ui/core/styles';
import { RouteComponentProps } from 'react-router-dom';
import { IModelProviderContext } from 'rev-ui/lib/provider/ModelProvider';
import { IModelManagerProp, withModelManager } from 'rev-ui';
import ArrowBack from '@material-ui/icons/ArrowBack';
import { IPerspective, IView, viewManager } from '../client/ViewManager';

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

export interface ICRMViewContextArgs {
    [key: string]: string;
}

export interface ICRMViewContext {
    url: string;
    perspective: IPerspective;
    view: IView;
    primaryKeyValue?: any;

    changePerspective(perspectiveName: string, viewName: string, args?: ICRMViewContextArgs): void;
}

export const CRMViewContext = React.createContext<ICRMViewContext>(null);

export interface ICRMViewManagerState {
    viewContext: ICRMViewContext;
}

export interface ICRMViewManagerProps extends RouteComponentProps<any>, WithStyles<keyof typeof styles> {}

class CRMViewManagerC extends React.Component<ICRMViewManagerProps & IModelManagerProp, ICRMViewManagerState> {

    constructor(props: ICRMViewManagerProps & IModelManagerProp, context: IModelProviderContext) {
        super(props, context);

        const ctx = this.updateViewContextFromLocation({
            url: '',
            perspective: null,
            view: null,
            primaryKeyValue: null,
            changePerspective: (p, v, k) => this.changePerspective(p, v, k)
        });
        console.log('initial viewContext:', ctx);

        this.state = {
            viewContext: ctx
        };
    }

    updateViewContextFromLocation(currentCtx: ICRMViewContext = null) {
        const newCtx: ICRMViewContext = {...currentCtx};
        newCtx.url = this.props.location.pathname + this.props.location.search;

        const { perspectiveName, perspectiveViewName } = this.props.match.params;
        newCtx.perspective = viewManager.getPerspective(perspectiveName);
        const viewName = viewManager.getPerspectiveViewName(perspectiveName, perspectiveViewName);
        newCtx.view = viewManager.getView(viewName);

        const search = new URLSearchParams(this.props.location.search);
        const meta = this.props.modelManager.getModelMeta(newCtx.view.model);

        if (meta.primaryKey) {
            const pkVal: any = search.get(meta.primaryKey);
            if (pkVal) {
                // TODO: We should probably use field meta for this rather than raw value
                if (!isNaN(pkVal)) {
                    newCtx.primaryKeyValue = Number.parseFloat(pkVal);
                }
                else {
                    newCtx.primaryKeyValue = pkVal;
                }
            }
            else {
                newCtx.primaryKeyValue = null;
            }
        }

        return newCtx;
    }

    componentDidUpdate() {
        const newUrl = this.props.location.pathname + this.props.location.search;
        if (this.state.viewContext.url != newUrl) {
            console.log('new URL:', newUrl);
            const ctx = this.updateViewContextFromLocation(this.state.viewContext);
            console.log('new viewContext:', ctx);
            this.setState({
                viewContext: ctx
            });
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

    render() {
        const ctx = this.state.viewContext;
        if (!ctx || !ctx.perspective || !ctx.view) {
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
                    <CRMViewContext.Provider value={this.state.viewContext}>
                        <Toolbar>
                            <ArrowBack style={{marginRight: 10, cursor: 'pointer'}} onClick={() => this.props.history.goBack()} />
                            <Typography variant="title" color="inherit">
                                {ctx.perspective.title}
                            </Typography>
                        </Toolbar>
                        <div className={this.props.classes.viewWrapper}>
                            {ctx.view.component}
                        </div>
                    </CRMViewContext.Provider>
                </div>
            );
        }
    }
}

export const CRMViewManager = withModelManager(withStyles(styles)(CRMViewManagerC)) as any;

export interface ICRMViewContextProp {
    viewContext: ICRMViewContext;
}

export function withCRMViewContext<P>(Component: React.ComponentType<P & ICRMViewContextProp>): React.ComponentType<P> {
    return (props: P) => (
        <CRMViewContext.Consumer>{(ctx) => (
            <Component viewContext={ctx} {...props} />
        )}</CRMViewContext.Consumer>
    );
}
