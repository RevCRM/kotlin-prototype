
import * as React from "react"
import { MockApolloClient } from "../../__testutils__/mockapollo"

let mockClient = new MockApolloClient()

export function setClient(client: MockApolloClient) {
    mockClient = client
}

export function withApolloClient(Component: React.ComponentType<any>) {
    return (props: any) => <Component client={mockClient} {...props} />
}
