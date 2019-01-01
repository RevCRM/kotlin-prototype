import { DocumentNode, SelectionNode, FieldNode } from "graphql"

export interface IEntityQueryOptions {
    entity: string
    fields: string[]
}

/**
 * Returns a flat GraphQL query for the specified entity, e.g.:
 *   query {
 *     Account (where: { name: "bob" }) {
 *       results {
 *         field1,
 *         field2
 *       }
 *       meta {
 *         totalCount
 *       }
 *     }
 *   }
 *
 * We dont use the gql tag here because the fields to be queried are metadata-driven
 */
export function getEntityQuery(options: IEntityQueryOptions): DocumentNode {
    const fieldSelections: SelectionNode[] = options.fields.map(field => {
        const fieldNode: FieldNode = {
            kind: "Field",
            name: {
                kind: "Name",
                value: field
            }
        }
        return fieldNode
    })
    const queryAST: DocumentNode = {
        kind: "Document",
        definitions: [
            {
                kind: "OperationDefinition",
                operation: "query",
                variableDefinitions: [
                    {
                        kind: "VariableDefinition",
                        variable: {
                            kind: "Variable",
                            name: { kind: "Name", value: "where" }
                        },
                        type: { kind: "NamedType", name: { kind: "Name", value: "JSON"}},
                    },
                ],
                selectionSet: {
                    kind: "SelectionSet",
                    selections: [
                        {
                            kind: "Field",
                            name: {
                                kind: "Name",
                                value: options.entity
                            },
                            arguments: [
                                {
                                    kind: "Argument",
                                    name: { kind: "Name", value: "where" },
                                    value: { kind: "Variable", name: { kind: "Name", value: "where" }},
                                }
                            ],
                            selectionSet: {
                                kind: "SelectionSet",
                                selections: [
                                    {
                                        kind: "Field",
                                        name: {
                                            kind: "Name",
                                            value: "results"
                                        },
                                        selectionSet: {
                                            kind: "SelectionSet",
                                            selections: fieldSelections
                                        },
                                    }
                                ]
                            }
                        }
                    ]
                }
            }
        ],
    }
    return queryAST
}
