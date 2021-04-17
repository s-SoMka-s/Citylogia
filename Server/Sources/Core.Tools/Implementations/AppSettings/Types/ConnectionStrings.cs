using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using System;

namespace Citylogia.Server.Core.Tools.Implementations.AppSettings.Types
{
    public class ConnectionStrings : IConnectionStrings
    {
        public ConnectionStrings()
        {
            Postgres = Environment.GetEnvironmentVariable("POSTGRES_CONNECTION");
        }
        public string Postgres { get; set; }
    }
}
