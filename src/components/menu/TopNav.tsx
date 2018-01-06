import * as React from 'react';
import { connect } from 'react-redux';

import AppBar from 'material-ui/AppBar';
import { setLeftNavOpen } from './store/index';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import MenuIcon from 'material-ui-icons/Menu';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';
import { withStyles, WithStyles } from 'material-ui/styles';

export interface ITopNavDispatchProps {
    onMenuButtonClick: () => void;
}

const styles: any = {
    root: {
        width: '100%',
    },
    flex: {
        flex: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    }
};

export class TopNavC extends React.Component<ITopNavDispatchProps & WithStyles> {
    render() {
        return (
            <div className={this.props.classes.root}>
                <AppBar position="fixed">
                    <Toolbar>
                        <IconButton color="contrast" aria-label="Menu" className={this.props.classes.menuButton}
                            onClick={this.props.onMenuButtonClick}>
                            <MenuIcon />
                        </IconButton>
                        <Typography type="title" color="inherit" className={this.props.classes.flex}>
                            RevCRM
                        </Typography>
                        <Button color="contrast">Login</Button>
                    </Toolbar>
                </AppBar>
            </div>
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

export const TopNav = connect(null, mapDispatchToProps)(withStyles(styles)(TopNavC));
