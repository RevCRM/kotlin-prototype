import * as React from 'react';
import { connect } from 'react-redux';

import AppBar from 'material-ui/AppBar';
import { setLeftNavOpen } from './store/index';

export interface ITopNavDispatchProps {
    onMenuButtonTouchTap: () => void;
}

export class TopNavC extends React.Component<void & ITopNavDispatchProps, void> {

    render() {
        return (
            <AppBar title="RevCRM" onLeftIconButtonTouchTap={this.props.onMenuButtonTouchTap} />
        );
    }

}

function mapDispatchToProps(dispatch: any): ITopNavDispatchProps {
    return {
        onMenuButtonTouchTap: () => {
            dispatch(setLeftNavOpen(true));
        }
    };
}

export const TopNav = connect(null, mapDispatchToProps)(TopNavC) as any;
