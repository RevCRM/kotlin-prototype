import * as React from "react"
import { MuiThemeProvider, createMuiTheme } from "@material-ui/core/styles"
import CssBaseline from "@material-ui/core/CssBaseline"
import { Navigation } from "./nav/Navigation"
import { ApolloClient } from "apollo-client"
import { setContext } from "apollo-link-context"
import { HttpLink } from "apollo-link-http"
import { InMemoryCache } from "apollo-cache-inmemory"
import { ApolloProvider } from "react-apollo"
import { AppLoader } from "./AppLoader"
import { AuthContextProvider } from "./auth/AuthContext"
import { createHashHistory } from "history"
import { ViewManager } from "./views/ViewManager"
import { View } from "./views/View"
import { register as registerDashboard } from "../modules/revcrm-dashboard"
import { register as registerAccounts } from "../modules/revcrm-accounts"
import { register as registerEtc } from "../modules/revcrm-etc"
import { UI } from "../UIManager"
import { AuthProvider } from "../auth/AuthProvider"
import { MetadataContextProvider } from "./meta/Metadata"

const theme = createMuiTheme()
const history = createHashHistory()

const authProvider = new AuthProvider()

const httpLink = new HttpLink({ uri: "/graphql" })
const authLink = setContext(async (_, { headers }) => {
    const token = await authProvider.currentToken()
    return {
        headers: {
            ...headers,
            authorization: token ? `Bearer ${token}` : "",
        }
    }
})
const cache = new InMemoryCache()
const client = new ApolloClient({
    link: authLink.concat(httpLink),
    cache
})

// register modules
registerDashboard(UI)
registerAccounts(UI)
registerEtc(UI)

export const App = () => (
    <MuiThemeProvider theme={theme}>
        <CssBaseline />
        <ApolloProvider client={client}>
            <AuthContextProvider provider={authProvider} history={history}>
                <MetadataContextProvider client={client}>
                    <AppLoader>
                        <ViewManager history={history}>
                            <Navigation>
                                <View />
                            </Navigation>
                        </ViewManager>
                    </AppLoader>
                </MetadataContextProvider>
            </AuthContextProvider>
        </ApolloProvider>
    </MuiThemeProvider>
)
