import { DocumentNode } from "graphql"
import {getFieldSelections, getSelectionSet, RECORD_NAME_FIELD} from "./helpers"
import { IEntityMetadata, IMetadataContext } from "../components/data/Metadata"

export interface IEntityQueryOptions {
    meta: IMetadataContext
    entity: IEntityMetadata
    fields: string[]
}

export interface IEntityQueryResults<T = any> {
    [entityName: string]: {
        results: T[]
        meta: {
            limit: number
            offset: number
            totalCount: number
        }
    }
}

/**
 * Returns a GraphQL query AST with all fields for the specified entity, e.g.:
 *   query {
 *     Account (where: { name: "bob" }) {
 *       results {
 *         field1,
 *         field2
 *       }
 *       meta {
 *         limit
 *         offset
 *         totalCount
 *       }
 *     }
 *   }
 *
 * We dont use the gql tag here because the fields to be queried are metadata-driven
 */
export function getEntityQuery(options: IEntityQueryOptions): DocumentNode {
    const selections = getFieldSelections(options.meta, options.entity, options.fields)
    const fieldSelectionSet = getSelectionSet(selections)
    const metaFieldSelectionSet = getSelectionSet({"limit": true, "offset": true, "totalCount": true})
    return {
        kind: "Document",
        definitions: [
            {
                kind: "OperationDefinition",
                operation: "query",
                variableDefinitions: [
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "where" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "JSON" } },
                    },
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "limit" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "PositiveInt" } },
                    },
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "offset" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "NonNegativeInt" } },
                    },
                ],
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        {
                            kind: "Field",
                            name: { kind: "Name", value: options.entity.name },
                            arguments: [
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "where" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "where" } },
                                },
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "limit" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "limit" } },
                                },
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "offset" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "offset" } },
                                },
                            ],
                            selectionSet: {
                                kind: "SelectionSet",
                                selections: [
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "results" },
                                        selectionSet: fieldSelectionSet
                                    },
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "meta" },
                                        selectionSet: metaFieldSelectionSet
                                    }
                                ]
                            }
                        }
                    ]
                }
            }
        ],
    }
}

export interface IReferencedEntityQueryOptions {
    meta: IMetadataContext
    entity: IEntityMetadata
}

export interface IReferencedEntityResult {
    id: string
    record_name?: string
}

/**
 * Returns a GraphQL query AST for referenced entity searches (by name). e.g.:
 *   query {
 *     Account (where: { _text: { _search: "record desc" } }) {
 *       results {
 *         id
 *         record_name
 *       }
 *       meta { totalCount... }
 *     }
 *   }
 *
 * We dont use the gql tag here because the fields to be queried are metadata-driven
 */
export function getReferencedEntityQuery(options: IReferencedEntityQueryOptions): DocumentNode {
    const { entity } = options
    const fieldSelections = { [entity.idField]: true }
    const recordNameField = entity.fields.find(f => f.name == RECORD_NAME_FIELD)
    if (recordNameField)
        fieldSelections[recordNameField.name] = true
    const fieldSelectionSet = getSelectionSet(fieldSelections)
    const metaFieldSelectionSet = getSelectionSet({"limit": true, "offset": true, "totalCount": true})
    return {
        kind: "Document",
        definitions: [
            {
                kind: "OperationDefinition",
                operation: "query",
                variableDefinitions: [
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "where" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "JSON" } },
                    },
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "limit" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "PositiveInt" } },
                    },
                    {
                        kind: "VariableDefinition",
                        variable: { kind: "Variable", name: { kind: "Name", value: "offset" } },
                        type: { kind: "NamedType", name: { kind: "Name", value: "NonNegativeInt" } },
                    },
                ],
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        {
                            kind: "Field",
                            name: { kind: "Name", value: options.entity.name },
                            arguments: [
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "where" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "where" } },
                                },
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "limit" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "limit" } },
                                },
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "offset" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "offset" } },
                                },
                            ],
                            selectionSet: {
                                kind: "SelectionSet",
                                selections: [
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "results" },
                                        selectionSet: fieldSelectionSet
                                    },
                                    {
                                        kind: "Field",
                                        name: { kind: "Name", value: "meta" },
                                        selectionSet: metaFieldSelectionSet
                                    }
                                ]
                            }
                        }
                    ]
                }
            }
        ],
    }
}
