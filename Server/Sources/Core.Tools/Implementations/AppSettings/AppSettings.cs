
using Citylogia.Server.Core.Tools.Implementations.AppSettings.Types;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings;
using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Core.Tools.Implementations.AppSettings.Types;
using Core.Tools.Interfaces.AppSettings.Types;
using Microsoft.Extensions.Configuration;

namespace Citylogia.Server.Core.Tools.Implementations.AppSettings
{
    public class AppSettings : IAppSettings
    {
        public AppSettings(IConfiguration configuration)
        {
            ConnectionStrings = new ConnectionStrings();
            AuthParameters = configuration.GetSection("Auth").Get<AuthParameters>();
        }

        #region IAppSettings

        public IConnectionStrings ConnectionStrings { get; }
        public IAuthParameters AuthParameters { get; }

        #endregion
    }
}
