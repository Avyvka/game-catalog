import {
  BaseRecord,
  DataProvider,
  GetListParams,
  GetListResponse,
} from "@refinedev/core";
import simpleRestDataProvider, { axiosInstance } from "@refinedev/simple-rest";
import { AxiosInstance } from "axios";

export const springDataProvider = (
  apiUrl: string,
  httpClient: AxiosInstance = axiosInstance
): Omit<Required<DataProvider>, "createMany" | "updateMany" | "deleteMany"> => {
  const delegate = simpleRestDataProvider(apiUrl, httpClient);
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
            size: pageSize,
          },
        }),
        delegate.custom({
          url: `${url}/count`,
          method,
          headers,
        }),
      ]);

      return {
        data: result.data as TData[],
        total: total.data as never as number,
      };
    },
  };
};
