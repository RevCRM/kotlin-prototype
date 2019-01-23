import { DocumentNode } from "graphql"
import { getSelectionSet, IFieldSelections } from "./helpers"
import { IEntityMetadata } from "../components/meta/Metadata"

export interface IEntityQueryOptions {
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
 * Returns a GraphQL query AST for the specified entity, e.g.:
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
    const selections: IFieldSelections = {}
    options.fields.forEach(fieldName => {
        const field = options.entity.fields.find(f => f.name == fieldName)!
        if (field.type != "EmbeddedEntityField") {
            selections[fieldName] = true
        }
    })
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
