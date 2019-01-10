
import * as React from "react"
import { Omit } from "../../types"
import gql from "graphql-tag"
import { withAuthContext, IAuthContextProp } from "../auth/AuthContext"
import { IApolloClientProp, withApolloClient } from "../../graphql/withApolloClient"
import { LoadState } from "../utils/types"

const METADATA_QUERY = gql`
    query {
        Metadata {
            entities {
                name
                fields {
                    name
                    label
                    type
                    nullable
                    properties
                    constraints
                }
            }
        }
    }
`

interface IMetadataQueryResponse {
    Metadata: {
        entities: IEntityMetadata[]
    }
}

export interface IFieldMetadata {
    name: string
    label: string
    type: string
    nullable: boolean
    properties: {
        [propertyName: string]: string
    }
    constraints: {
        [propertyName: string]: string
    }
}

export interface IEntityMetadata {
    name: string
    fields: IFieldMetadata[]
}

export interface IMetadataContext {
    metaState: LoadState
    getEntity(name: string): IEntityMetadata | undefined
}

export interface IMetadataContextProviderProps extends
                    IAuthContextProp,
                    IApolloClientProp {
}

export interface IMetadataContextProviderState {
    metaState: LoadState
    entities: IEntityMetadata[]
}

export const MetadataContext = React.createContext<IMetadataContext>(null as any)

export const MetadataContextProvider = withAuthContext(withApolloClient(
    class extends React.Component<IMetadataContextProviderProps, IMetadataContextProviderState> {

    constructor(props: any) {
        super(props)
        this.state = {
            metaState: "not_loaded",
            entities: []
        }
    }

    async initialise() {
        this.setState({ metaState: "loading" })
        const res = await this.props.client.query<IMetadataQueryResponse>({
            query: METADATA_QUERY
        })
        if (res.errors && res.errors.length) {
            this.setState({ metaState: "load_error" })
            console.error("Failed to load Metadata", res.errors)
        }
        else {
            console.log("Metadata loaded", res.data)
            this.setState({
                metaState: "loaded",
                entities: res.data.Metadata.entities
            })
        }
    }

    componentDidMount() {
        if (this.props.auth.authState == "logged_in" && this.state.metaState == "not_loaded") {
            this.initialise()
        }
    }
    componentDidUpdate() {
        if (this.props.auth.authState == "logged_in" && this.state.metaState == "not_loaded") {
            this.initialise()
        }
    }

    getEntity = (name: string) => {
        return this.state.entities.find(entity =>
            entity.name == name
        )
    }

    render() {
        const metaContext: IMetadataContext = {
            metaState: this.state.metaState,
            getEntity: this.getEntity
        }
        return (
            <MetadataContext.Provider value={metaContext}>
                {this.props.children}
            </MetadataContext.Provider>
        )
    }

}))

export interface IMetadataContextProp {
    meta: IMetadataContext
}

export function withMetadataContext<
    TComponentProps extends IMetadataContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IMetadataContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: any): React.ReactElement<TComponentProps> => (
        <MetadataContext.Consumer>{(meta) => (
            <Component meta={meta} {...props} />
        )}</MetadataContext.Consumer>
    )
}
