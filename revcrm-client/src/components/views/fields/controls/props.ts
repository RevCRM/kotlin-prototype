import * as React from "react"
import { ColspanOptions } from "../../Grid"
import { IFieldMetadata, IMetadataContext } from "../../../meta/Metadata"
import { IFormContext } from "../../FormView"

export interface IStandardComponentProps {
    style?: Partial<React.CSSProperties>
}

// TODO: Move This
export interface IFieldError {
    message: string
    code?: string
}

export interface IFieldComponentProps extends IStandardComponentProps  {
    meta: IMetadataContext
    form: IFormContext
    field: IFieldMetadata
    label: string
    colspanNarrow: ColspanOptions
    colspan: ColspanOptions
    colspanWide: ColspanOptions
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
