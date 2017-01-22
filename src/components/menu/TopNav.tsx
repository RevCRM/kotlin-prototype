import * as React from 'react';
import { connect } from 'react-redux';

import AppBar from 'material-ui/AppBar';
import { setLeftNavOpen } from './store/index';

interface ITopNavProps {
    onMenuButtonTouchTap: () => void;
}

class TopNav extends React.Component<ITopNavProps, void> {

    render() {
        return (
            <AppBar title="RevCRM" onLeftIconButtonTouchTap={this.props.onMenuButtonTouchTap} />
        );
    }

}

function mapDispatchToProps(dispatch: any) {
    return {
        onMenuButtonTouchTap: () => {
            dispatch(setLeftNavOpen(true));
        }
    };
}

export default connect(null, mapDispatchToProps)(TopNav);
