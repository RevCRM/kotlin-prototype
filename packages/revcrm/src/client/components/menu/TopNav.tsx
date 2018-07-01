import * as React from 'react';
import { connect } from 'react-redux';

import AppBar from '@material-ui/core/AppBar';
import { setLeftNavOpen } from './store/index';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import Typography from '@material-ui/core/Typography';
import { withStyles, WithStyles } from '@material-ui/core/styles';

export interface ITopNavDispatchProps {
    onMenuButtonClick: () => void;
}

export const styles = {
    root: {
        width: '100%',
        '@media print': {
            display: 'none'
        }
    },
    flex: {
        flex: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    }
};

export class TopNavC extends React.Component<ITopNavDispatchProps & WithStyles<keyof typeof styles>> {
    render() {
        return (
            <div className={this.props.classes.root}>
                <AppBar position="fixed">
                    <Toolbar>
                        <IconButton color="inherit" aria-label="Menu" className={this.props.classes.menuButton}
                            onClick={this.props.onMenuButtonClick}>
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={this.props.classes.flex}>
                            RevCRM
                        </Typography>
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

export const TopNav = connect(null, mapDispatchToProps)(withStyles(styles)(TopNavC)) as any;
