import * as React from 'react';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { Navigation } from './nav/Navigation';
import { GoogleAuth } from './auth/GoogleAuth';
import { ApolloClient } from 'apollo-client';
import { HttpLink } from 'apollo-link-http';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { ApolloLink } from 'apollo-link';
import { ApolloProvider } from 'react-apollo';
import { Dashboard } from './dashboard/Dashboard';
import { AppLoader } from './AppLoader';
import { AuthContextProvider } from './auth/AuthContext';

const theme = createMuiTheme();

const cache = new InMemoryCache();
const client = new ApolloClient({
    link: ApolloLink.from([
        new HttpLink({ uri: '/graphql' })
    ]),
    cache
});

export const App = () => (
    <MuiThemeProvider theme={theme}>
        <CssBaseline />
        <ApolloProvider client={client}>
            <AuthContextProvider>
                <AppLoader>
                    <Navigation>
                        <Dashboard />
                        {/* <GoogleAuth /> */}
                        {/* <Routes /> */}
                    </Navigation>
                </AppLoader>
            </AuthContextProvider>
        </ApolloProvider>
    </MuiThemeProvider>
);
