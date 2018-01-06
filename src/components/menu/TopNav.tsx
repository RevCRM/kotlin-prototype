import * as React from 'react';
import { connect } from 'react-redux';

import AppBar from 'material-ui/AppBar';
import { setLeftNavOpen } from './store/index';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import MenuIcon from 'material-ui-icons/Menu';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';

export interface ITopNavDispatchProps {
    onMenuButtonClick: () => void;
}

export class TopNavC extends React.Component<ITopNavDispatchProps> {
    render() {
        return (
            <AppBar>
                <Toolbar>
                    <IconButton color="contrast" aria-label="Menu" onClick={this.props.onMenuButtonClick}>
                        <MenuIcon />
                    </IconButton>
                    <Typography type="title" color="inherit">
                        RevCRM
                    </Typography>
                    <Button color="contrast">Login</Button>
                </Toolbar>
            </AppBar>
        );
    }
}

function mapDispatchToProps(dispatch: any): ITopNavDispatchProps {
    return {
        onMenuButtonClick: () => {
            dispatch(setLeftNavOpen(true));
        }
    };
}

export const TopNav = connect(null, mapDispatchToProps)(TopNavC) as any;
