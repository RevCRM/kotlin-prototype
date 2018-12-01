import * as React from 'react';

import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { withStyles, WithStyles, createStyles, Theme } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import { Hidden, Icon } from '@material-ui/core';
import { LeftNav } from './LeftNav';
import { CONFIG } from '../../config';

const leftNavWidth = 300;

export const styles = (theme: Theme) => createStyles({
    root: {
        display: 'flex'
    },
    appBar: {
        zIndex: theme.zIndex.drawer + 1,
        '@media print': {
            display: 'none'
        }
    },
    leftNav: {
        [theme.breakpoints.up('sm')]: {
            width: leftNavWidth,
            flexShrink: 0,
        },
    },
    leftNavPaper: {
        width: leftNavWidth
    },
    toolbar: theme.mixins.toolbar,
    flex: {
        flex: 1,
    },
    menuButton: {
        marginRight: 16,
        [theme.breakpoints.up('sm')]: {
            display: 'none',
        },
    },
    mainBody: {
    }
});

export interface INavigationState {
    leftNavOpen: boolean;
}

export const Navigation = withStyles(styles)(
    class extends React.Component<WithStyles<typeof styles>, INavigationState> {

    constructor(props: any) {
        super(props);
        this.state = {
            leftNavOpen: false
        };
    }

    onClickMenuButton = () => {
        this.setState((prevState) => ({
            leftNavOpen: !prevState.leftNavOpen
        }));
    }

    render() {
        return (
            <div className={this.props.classes.root}>

                <AppBar position="fixed" className={this.props.classes.appBar}>
                    <Toolbar>
                        <IconButton color="inherit" aria-label="Menu" className={this.props.classes.menuButton}
                            onClick={this.onClickMenuButton}>
                            <Icon>menu</Icon>
                        </IconButton>
                        <Typography variant="h6" color="inherit" className={this.props.classes.flex}>
                            {CONFIG.appTitle}
                        </Typography>
                        <IconButton
                            onClick={() => alert('test')}
                            color="inherit"
                        >
                            <Icon>account_circle</Icon>
                        </IconButton>
                    </Toolbar>
                </AppBar>

                <nav className={this.props.classes.leftNav}>
                    {/* The implementation can be swap with js to avoid SEO duplication of links. */}
                    <Hidden smUp implementation="css">
                        <Drawer
                            variant="temporary"
                            anchor="left"
                            open={this.state.leftNavOpen}
                            onClose={this.onClickMenuButton}
                            classes={{
                                paper: this.props.classes.leftNavPaper,
                            }}
                            ModalProps={{
                                keepMounted: true, // Better open performance on mobile.
                            }} >
                            <LeftNav />
                        </Drawer>
                    </Hidden>
                    <Hidden xsDown implementation="css">
                        <Drawer
                            classes={{
                                paper: this.props.classes.leftNavPaper,
                            }}
                            variant="permanent"
                            open >
                            <div className={this.props.classes.toolbar} />
                            <LeftNav />
                        </Drawer>
                    </Hidden>
                </nav>

                <main className={this.props.classes.mainBody}>
                    <div className={this.props.classes.toolbar} />
                    {this.props.children}
                </main>

            </div>
        );
    }

});