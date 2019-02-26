
export class MockApolloClient {
    query = jest.fn()

    setQueryResult<T = any>(
        result: { data: T }
    ) {
        this.query.mockResolvedValue(result)
    }
}
