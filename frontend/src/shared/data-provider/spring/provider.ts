import {
  BaseRecord,
  DataProvider,
  GetListParams,
  GetListResponse
} from "@refinedev/core";
import simpleRestDataProvider from "@refinedev/simple-rest";

export const springDataProvider = (
  apiUrl: string
): Omit<Required<DataProvider>, "createMany" | "updateMany" | "deleteMany"> => {
  const delegate = simpleRestDataProvider(apiUrl);
  return {
    ...delegate,
    getList: async <TData extends BaseRecord = BaseRecord>(
      params: GetListParams
    ): Promise<GetListResponse<TData>> => {
      const url = `${apiUrl}/${params.resource}`;
      const { currentPage = 1, pageSize = 10 } = params.pagination ?? {};
      const { headers, method } = params.meta ?? {};

      const [result, total] = await Promise.all([
        delegate.custom({
          url,
          method,
          headers,
          query: {
            page: currentPage - 1,
            size: pageSize
          }
        }),
        delegate.custom({
          url: `${url}/count`,
          method,
          headers
        })
      ]);

      return {
        data: result.data as TData[],
        total: total.data as never as number
      };
    }
  };
};
