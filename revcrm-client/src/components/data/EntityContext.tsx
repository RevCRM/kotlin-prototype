
import * as React from "react"
import { LoadState } from "../utils/types"
import { IEntityMetadata, IFieldMetadata } from "./Metadata"
import { IEntityMutationResult } from "../../graphql/mutations"
import { Omit } from "../../types"

export type EntityViewMode = "view" | "edit"

export interface IEntityContext {
    name: string
    meta: IEntityMetadata
    mode: EntityViewMode
    loadState: LoadState
    data: any
    dirtyFields: string[]
    onFieldChange(field: IFieldMetadata, value: any): void
    save(): Promise<IEntityMutationResult>
}

export const EntityContext = React.createContext<IEntityContext>(null as any)

export interface IEntityContextProp {
    entity: IEntityContext
}

export function withEntityContext<
    TComponentProps extends IEntityContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IEntityContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: any): React.ReactElement<TComponentProps> => (
        <EntityContext.Consumer>{(entity) => (
            <Component entity={entity} {...props} />
        )}</EntityContext.Consumer>
    )
}
