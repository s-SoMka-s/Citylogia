using System.Threading.Tasks;

namespace Core.Tools.Interfaces.Auth
{
    public interface IJwtManager
    {
        Task<TokenPair> GeneratePairAsync(long userId);
    }
}
