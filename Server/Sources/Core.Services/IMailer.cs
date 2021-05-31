using System.Threading.Tasks;

namespace Core.Services
{
    public interface IMailer
    {
        public Task SendAsync(string to, string body);
    }
}