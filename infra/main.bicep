@description('Azure region for deployment (UK South)')
param location string = 'uksouth'

@description('Name of the Container App Environment')
param envName string = 'cae-devops-mcp-prod'

@description('Name of the Container App')
param appName string = 'ca-devops-mcp-server'

@description('Docker image tag from GitHub Container Registry')
param containerImage string = 'ghcr.io/your-username/azure-devops-mcp-server:latest'

resource containerAppEnv 'Microsoft.App/managedEnvironments@2024-03-01' = {
  name: envName
  location: location
  properties: {}
}

resource mcpContainerApp 'Microsoft.App/containerApps@2024-03-01' = {
  name: appName
  location: location
  properties: {
    managedEnvironmentId: containerAppEnv.id
    configuration: {
      activeRevisionsMode: 'Single'
    }
    template: {
      containers: [
        {
          name: 'devops-mcp-server'
          image: containerImage
          resources: {
            cpu: json('0.5')
            memory: '1.0Gi'
          }
        }
      ]
      scale: {
        minReplicas: 0
        maxReplicas: 1
      }
    }
  }
}
