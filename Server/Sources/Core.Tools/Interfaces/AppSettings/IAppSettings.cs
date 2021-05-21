using Citylogia.Server.Core.Tools.Interfaces.AppSettings.Types;
using Core.Tools.Interfaces.AppSettings.Types;

namespace Citylogia.Server.Core.Tools.Interfaces.AppSettings
{
    public interface IAppSettings
    {
        IConnectionStrings ConnectionStrings { get;}
        IAuthParameters AuthParameters { get; }
    }
}
