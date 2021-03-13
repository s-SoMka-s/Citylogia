
using Citylogia.Server.Core.Tools.Implementations.AppSettings.Types;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Microsoft.Extensions.Configuration;

namespace Citylogia.Server.Core.Tools.Implementations.AppSettings
{
    public class AppSettings : IAppSettings
    {
        public AppSettings(IConfiguration configuration)
        {
            ConnectionStrings = new ConnectionStrings();
        }

        #region IAppSettings

        public IConnectionStrings ConnectionStrings { get; }

        #endregion
    }
}
