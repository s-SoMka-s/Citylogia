#
#
# STAGE 1: Build
#
#
FROM mcr.microsoft.com/dotnet/sdk:5.0-buster-slim AS builder

WORKDIR /Sources
COPY . .
ENV POSTGRES_CONNECTION="Host=34.134.77.166;Port=5432;Database=citylogia;User ID=postgres;Password=citylogia;"
RUN dotnet publish /Sources/Citylogia.Server/Citylogia.Server.csproj --output /app/ --configuration Release

#
#
# STAGE 2: Launch
#
#
FROM mcr.microsoft.com/dotnet/aspnet:5.0-buster-slim AS launcher
WORKDIR /app

COPY --from=builder /app .

ENTRYPOINT ["dotnet", "Citylogia.Server.dll"]