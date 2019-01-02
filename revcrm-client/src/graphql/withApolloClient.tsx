import * as React from "react"
import ApolloClient from "apollo-client"
import { withApollo } from "react-apollo"
import { Omit } from "../types"

export interface IApolloClientProp {
    client: ApolloClient<any>
}

// Unfortunately withApollo HOC typing is not compatible with our HOCs
// so we are using this custom HOC
export function withApolloClient<
    TComponentProps extends IApolloClientProp,
    TWrapperProps = Omit<TComponentProps, keyof IApolloClientProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return withApollo((props: any) =>
        <Component {...props} />
    )
}
