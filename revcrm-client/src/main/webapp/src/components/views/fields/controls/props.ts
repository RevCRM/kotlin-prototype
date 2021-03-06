import * as React from "react"
import { ColspanOptions } from "../../Grid"
import { IFieldMetadata, IMetadataContext } from "../../../data/Metadata"
import { IEntityContext } from "../../../data/EntityContext"

export interface IStandardComponentProps {
    style?: Partial<React.CSSProperties>
}

// TODO: Move This
export interface IFieldError {
    message: string
    code?: string
}

export interface IFieldComponentProps extends IStandardComponentProps  {
    entity: IEntityContext
    meta: IMetadataContext
    field: IFieldMetadata
    label: string | false
    grid: {
        colspanNarrow: ColspanOptions
        colspan: ColspanOptions
        colspanWide: ColspanOptions
    } | null
    value: any
    errors: IFieldError[]
    disabled: boolean
    readonly: boolean
    onChange: (value: any) => void
    children: any
}

export function getStandardHTMLProps(props: any) {
    const sProps: IStandardComponentProps = {}
    function addProp(propName: keyof IStandardComponentProps) {
        if (typeof props[propName] != "undefined") {
            sProps[propName] = props[propName]
        }
    }
    addProp("style")
    return sProps
}
