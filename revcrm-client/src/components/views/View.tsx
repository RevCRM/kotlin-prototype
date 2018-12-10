
import * as React from 'react';
import { withViewManagerContext, IViewManagerContextProp } from './ViewManager';

export const View = withViewManagerContext((props: IViewManagerContextProp) => {
    const Component = props.viewManagerCtx.view.component;
    return <Component />;
});
