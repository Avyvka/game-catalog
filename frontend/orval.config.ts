export default {
  gameCatalogApi: {
    input: {
      target: "./src/shared/api/openapi.json"
    },
    output: {
      mode: "split",
      client: "react-query",
      target: "./src/shared/api/generated/api.ts",
      schemas: "./src/shared/api/generated/types",
    }
  }
}